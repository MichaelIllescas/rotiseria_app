// src/services/apiClient.js
import axios from "axios";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080", // tu backend de Spring Boot
  withCredentials: true,            // importante si usas sesiones/cookies
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
