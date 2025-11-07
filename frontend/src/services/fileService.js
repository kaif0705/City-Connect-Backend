import api from './api'; // Import our central Axios instance

/**
 * --- Slice 6, Phase 2: Upload a File ---
 * Calls the POST /api/v1/files/upload endpoint.
 *
 * @param {File} file - The file object from an <input type="file">
 * @returns {Promise<string>} - The web-accessible URL of the saved file (e.g., "/media/image.jpg")
 */
export const uploadFile = async (file) => {
  // 1. We must use FormData to send files to the backend
  const formData = new FormData();
  
  // 2. Append the file. The key "file" MUST match the
  //    @RequestParam("file") annotation in our FileController.java
  formData.append("file", file);

  try {
    // 3. Make the POST request
    // We pass the formData as the body.
    // We also must set the 'Content-Type' header to 'multipart/form-data'
    // so the backend knows how to handle it.
    const response = await api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    // 4. The backend returns { "url": "/media/..." }
    // We'll return just the URL string to the component.
    return response.data.url;

  } catch (error) {
    // Let the component handle the error and show a notification
    console.error("File upload failed:", error);
    
    // Re-throw a clean error message
    if (error.response && error.response.data && error.response.data.message) {
      throw new Error(error.response.data.message);
    } else {
      throw new Error("File upload failed. Please try again.");
    }
  }
};