import React, { SetStateAction } from 'react';

export const DownloadFileFromS3 = async (
  url: string,
  setLoading: React.Dispatch<SetStateAction<boolean>>,
  fileName?: string,
) => {
  setLoading(true);

  try {
    const response = await fetch(url);
    const blob = await response.blob();

    // Create a temporary link element
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);

    // Set the filename (optional)
    if (!fileName) {
      fileName = url.split('/').pop() || 'file';
      a.setAttribute('download', fileName);
    }
    if (fileName) {
      a.setAttribute('download', fileName);
    }
    // Hide the link element
    a.style.display = 'none';

    // Append the link to the body
    document.body.appendChild(a);

    // Simulate a click to trigger the download
    a.click();

    // Remove the link from the DOM
    document.body.removeChild(a);
  } catch (error) {
    console.log(error);
  } finally {
    setLoading(false);
  }
};
