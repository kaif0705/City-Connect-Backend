import axios from 'axios';

// 1. Define the Base URL for our Spring Boot backend.
// We are using the port 8081 and the /api/v1 prefix we defined.
const API_BASE_URL = 'http://localhost:8080/api/v1';

// 2. Create the central axios instance.
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  // We can add interceptors here later for auth (Slice 4)
});

// 3. Export the instance so other services can use it.
export default api;