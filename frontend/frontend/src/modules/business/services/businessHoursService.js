import apiClient from "../../../shared/services/apiClient";

export const businessHoursService = {
  getHours: async () => {
    const response = await apiClient.get("/api/settings/business-hours");
    return response.data;
  },

  updateHours: async (hours) => {
    const response = await apiClient.put("/api/settings/business-hours", hours);
    return response.data;
  },
};
