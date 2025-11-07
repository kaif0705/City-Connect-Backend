import React, { useState, useEffect } from "react";
import {
  getAllIssues,
  updateIssueStatus,
  deleteIssue,
} from "../services/issueService";
import { useNotification } from "../context/NotificationContext";

// Import MUI components
import {
  Container,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Card,
  CardContent,
  CardMedia, // 1. NEW IMPORT for images
  CardActions,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Chip,
} from "@mui/material";

// 2. DEFINE OUR BACKEND URL FOR MEDIA
const BACKEND_URL = "http://localhost:8080";

function AdminDashboardPage() {
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { showNotification } = useNotification();

  // ... (fetchIssues, handleStatusChange, and handleDelete functions are all the same)
  // Function to fetch all issues
  const fetchIssues = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getAllIssues();
      setIssues(data);
    } catch (apiError) {
      console.error("Failed to fetch issues:", apiError);
      const errorMessage =
        apiError.response &&
        apiError.response.data &&
        apiError.response.data.message
          ? apiError.response.data.message
          : "Could not load issues. Please try again later.";
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Handler for changing an issue's status
  const handleStatusChange = async (id, newStatus) => {
    try {
      const updatedIssue = await updateIssueStatus(id, newStatus);
      setIssues((prevIssues) =>
        prevIssues.map((issue) =>
          issue.id === id ? { ...issue, status: updatedIssue.status } : issue
        )
      );
      showNotification("Issue status updated successfully!", "success");
    } catch (apiError) {
      console.error("Failed to update status:", apiError);
      const errorMessage =
        apiError.response &&
        apiError.response.data &&
        apiError.response.data.message
          ? apiError.response.data.message
          : "Failed to update status.";
      showNotification(errorMessage, "error");
    }
  };

  // Handler for deleting an issue
  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this issue?")) {
      return;
    }
    try {
      await deleteIssue(id);
      setIssues((prevIssues) => prevIssues.filter((issue) => issue.id !== id));
      showNotification("Issue deleted successfully!", "success");
    } catch (apiError) {
      console.error("Failed to delete issue:", apiError);
      const errorMessage =
        apiError.response &&
        apiError.response.data &&
        apiError.response.data.message
          ? apiError.response.data.message
          : "Failed to delete issue.";
      showNotification(errorMessage, "error");
    }
  };

  // --- (Render Logic is the same) ---
  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "50vh",
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" component="h1" gutterBottom>
        Admin Dashboard
      </Typography>

      {issues.length === 0 ? (
        <Typography
          variant="h6"
          color="text.secondary"
          align="center"
          sx={{ mt: 5 }}
        >
          No issues found.
        </Typography>
      ) : (
        <Box>
          {issues.map((issue) => (
            <Card key={issue.id} sx={{ mb: 2, backgroundColor: "#f9f9f9" }}>
              {/* --- 3. ADD THIS BLOCK (Conditional Image) --- */}
              {issue.imageUrl && (
                <CardMedia
                  component="img"
                  height="300" // Set a fixed height for consistency
                  image={`${BACKEND_URL}${issue.imageUrl}`}
                  alt={issue.title}
                  sx={{ objectFit: "cover" }} // Ensures the image covers the area
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
                  Category: {issue.category} (Reported by:{" "}
                  {issue.submittedByUsername})
                </Typography>
                <Typography variant="body2" sx={{ mb: 2 }}>
                  {issue.description}
                </Typography>
                <Typography color="text.secondary" variant="caption">
                  Reported on: {new Date(issue.createdAt).toLocaleString()}
                </Typography>
              </CardContent>
              <CardActions sx={{ justifyContent: "flex-end", pr: 2, pb: 2 }}>
                <FormControl size="small" sx={{ minWidth: 150, mr: 1 }}>
                  <InputLabel>Change Status</InputLabel>
                  <Select
                    value={issue.status}
                    label="Change Status"
                    onChange={(e) =>
                      handleStatusChange(issue.id, e.target.value)
                    }
                  >
                    <MenuItem value="PENDING">Pending</MenuItem>
                    <MenuItem value="IN_PROGRESS">In Progress</MenuItem>
                    <MenuItem value="RESOLVED">Resolved</MenuItem>
                  </Select>
                </FormControl>

                <Button
                  size="small"
                  color="error"
                  variant="outlined"
                  onClick={() => handleDelete(issue.id)}
                >
                  Delete
                </Button>
              </CardActions>
            </Card>
          ))}
        </Box>
      )}
    </Container>
  );
}

export default AdminDashboardPage;
