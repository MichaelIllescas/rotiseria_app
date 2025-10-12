import { useState } from "react";
import { businessHoursService } from "../services/businessHoursService";

export const useBusinessHours = () => {
  const [hours, setHours] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchHours = async () => {
    try {
      const data = await businessHoursService.getHours();
      setHours(data);
    } catch (error) {
      console.error("Error al obtener horarios:", error);
    } finally {
      setLoading(false);
    }
  };

  const saveHours = async (newHours) => {
    await businessHoursService.updateHours(newHours);
    fetchHours();
  };

  return { hours, loading, fetchHours, saveHours };
};
