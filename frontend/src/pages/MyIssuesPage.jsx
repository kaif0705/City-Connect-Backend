import React, { useState, useEffect } from "react";
import { getMyIssues } from "../services/issueService";
import { useNotification } from "../context/NotificationContext"; // We'll import this just in case

// Import MUI components
import {
  Typography,
  Container,
  Card,
  CardContent,
  Box,
  CircularProgress,
  Alert,
  Chip,
  CardMedia, // 1. NEW IMPORT for images
} from "@mui/material";

// 2. DEFINE OUR BACKEND URL FOR MEDIA
const BACKEND_URL = "http://localhost:8080";

function MyIssuesPage() {
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { showNotification } = useNotification(); // Get the hook

  useEffect(() => {
    // Fetch issues when the component mounts
    const fetchIssues = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await getMyIssues();
        setIssues(data);
      } catch (apiError) {
        console.error("Failed to fetch my issues:", apiError);
        const errorMessage =
          apiError.response &&
          apiError.response.data &&
          apiError.response.data.message
            ? apiError.response.data.message
            : "Could not load your issues.";
        setError(errorMessage);
        // Show a snackbar notification on error
        showNotification(errorMessage, "error");
      } finally {
        setLoading(false);
      }
    };

    fetchIssues();
  }, []); // The empty array [] means this runs only once

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  // Show persistent error on the page
  if (error && issues.length === 0) {
    return <Alert severity="error">{error}</Alert>;
  }

  return (
    <Container maxWidth="md">
      <Typography variant="h4" component="h1" gutterBottom>
        My Submitted Issues
      </Typography>
      {issues.length === 0 ? (
        <Typography>You have not submitted any issues yet.</Typography>
      ) : (
        <Box>
          {issues.map((issue) => (
            <Card key={issue.id} sx={{ mb: 2 }}>
              {/* --- 3. ADD THIS BLOCK (Conditional Image) --- */}
              {issue.imageUrl && (
                <CardMedia
                  component="img"
                  height="300"
                  image={`${BACKEND_URL}${issue.imageUrl}`}
                  alt={issue.title}
                  sx={{ objectFit: "cover" }}
                />
              )}
              {/* --- END OF NEW BLOCK --- */}

              <CardContent>
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 1,
                  }}
                >
                  <Typography variant="h6" component="h2">
                    {issue.title}
                  </Typography>
                  <Chip
                    label={issue.status}
                    color={
                      issue.status === "RESOLVED"
                        ? "success"
                        : issue.status === "IN_PROGRESS"
                        ? "warning"
                        : "default"
                    }
                    size="small"
                  />
                </Box>
                <Typography color="text.secondary" gutterBottom>
                  Category: {issue.category}
                </Typography>
                <Typography variant="body2" sx={{ mb: 2 }}>
                  {issue.description}
                </Typography>
                <Typography color="text.secondary" variant="caption">
                  Reported on: {new Date(issue.createdAt).toLocaleDateString()}
                </Typography>
              </CardContent>
            </Card>
          ))}
        </Box>
      )}
    </Container>
  );
}

export default MyIssuesPage;
