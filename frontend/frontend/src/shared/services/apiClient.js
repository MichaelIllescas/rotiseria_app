// src/services/apiClient.js
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080", // tu backend de Spring Boot
  withCredentials: true,            // importante si usas sesiones/cookies
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
