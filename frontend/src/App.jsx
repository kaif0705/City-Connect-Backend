import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';

// We will create these two pages in the next steps.
// Don't worry if your editor shows an error for now.
// import SubmitPage from './pages/SubmitPage';
// import AdminDashboardPage from './pages/AdminDashboardPage';

/**
 * Main App component.
 * This will act as our main layout and router.
 */
function App() {
  
  // These are temporary placeholders until we create the pages
  const SubmitPage = () => <div>Submit Issue Page (Coming Soon)</div>;
  const AdminDashboardPage = () => <div>Admin Dashboard Page (Coming Soon)</div>;

  return (
    <div className="app-container">
      
      {/* 1. Navigation Bar */}
      <nav className="navbar">
        <Link to="/" className="nav-link">CityConnect</Link>
        <ul className="nav-menu">
          <li>
            <Link to="/" className="nav-link">Report an Issue</Link>
          </li>
          <li>
            <Link to="/admin" className="nav-link">Admin Dashboard</Link>
          </li>
        </ul>
      </nav>

      {/* 2. Page Content Area */}
      <div className="page-content">
        <Routes>
          <Route path="/" element={<SubmitPage />} />
          <Route path="/admin" element={<AdminDashboardPage />} />
          {/* We'll add routes for Login/Register in Slice 4 */}
        </Routes>
      </div>
      
    </div>
  );
}

export default App;