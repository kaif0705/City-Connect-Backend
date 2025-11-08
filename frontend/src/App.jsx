import React from 'react';
import { Routes, Route, Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';

// --- Import all our pages ---
import SubmitPage from './pages/SubmitPage';
import AdminDashboardPage from './pages/AdminDashboardPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MyIssuesPage from './pages/MyIssuesPage';
import IssueDetailPage from './pages/IssueDetailPage';
import ProfilePage from './pages/ProfilePage';

// --- Import our GUARDS ---
import ProtectedRoute from './components/ProtectedRoute';
import AdminRoute from './components/AdminRoute';

// --- Import MUI Components ---
import { AppBar, Toolbar, Typography, Button, Box, Container } from '@mui/material';

function App() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="app-container">
      
      {/* --- The "Smart" MUI Navigation Bar --- */}
      <AppBar position="static">
        <Toolbar>
          
          {/* --- THIS IS THE CHANGE --- */}
          {/* We use a Box to group the logo and title together */}
          <Box 
            component={RouterLink} 
            to="/" 
            sx={{ 
              display: 'flex', 
              alignItems: 'center', 
              flexGrow: 1, 
              textDecoration: 'none', 
              color: 'inherit' 
            }}
          >
            <Box
              component="img"
              src="/Logo.png"
              alt="MIT-Connect Logo"
              sx={{
                height: 40, // Set height for navbar
                mr: 2, // Margin-right to add space
              }}
            />
            <Typography variant="h6" component="div">
              MIT-Connect
            </Typography>
          </Box>
          {/* --- END OF CHANGE --- */}


          {isAuthenticated ? (
            // --- User is Logged In ---
            <Box>
              <Button component={RouterLink} to="/" color="inherit">
                Report an Issue
              </Button>
              <Button component={RouterLink} to="/my-issues" color="inherit">
                My Issues
              </Button>
              <Button component={RouterLink} to="/profile" color="inherit">
                Profile
              </Button>
              
              {user?.role === 'ROLE_ADMIN' && (
                <Button component={RouterLink} to="/admin" color="inherit">
                  Admin Dashboard
                </Button>
              )}
              
              <Button color="inherit" onClick={handleLogout}>
                Logout ({user?.username})
              </Button>
            </Box>
          ) : (
            // --- User is Logged Out ---
            <Box>
              <Button component={RouterLink} to="/login" color="inherit">
                Login
              </Button>
              <Button component={RouterLink} to="/register" color="inherit">
                Sign Up
              </Button>
            </Box>
          )}
        </Toolbar>
      </AppBar>
      
      {/* --- The Page Content Area --- */}
      <Container sx={{ mt: 4, mb: 4 }}>
        <Routes>
          {/* --- 1. Public Routes --- */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* --- 2. Protected Routes (for any logged-in user) --- */}
          <Route 
            path="/" 
            element={
              <ProtectedRoute>
                <SubmitPage />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/my-issues" 
            element={
              <ProtectedRoute>
                <MyIssuesPage />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/issue/:id" 
            element={
              <ProtectedRoute>
                <IssueDetailPage />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/profile" 
            element={
              <ProtectedRoute>
                <ProfilePage />
              </ProtectedRoute>
            } 
          />
          
          {/* --- 4. Admin-Only Route --- */}
          <Route 
            path="/admin" 
            element={
              <AdminRoute>
                <AdminDashboardPage />
              </AdminRoute>
            } 
          />
        </Routes>
      </Container>
      
    </div>
  );
}

export default App;