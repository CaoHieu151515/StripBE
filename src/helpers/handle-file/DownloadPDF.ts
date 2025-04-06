export function saveBlob(content: any, mimeType: string, filename: string, onlyDownload: boolean = false) {
  const a = window.document.createElement('a'); // Create "a" element
  const blob = new Blob([content], { type: mimeType }); // Create a blob (file-like object)
  const url = URL.createObjectURL(blob); // Create an object URL from blob
  a.setAttribute('href', url); // Set "a" element link
  a.setAttribute('download', filename); // Set download filename
  if (onlyDownload) {
    a.click();
    return;
  }
  window.open(url, '_blank');
  // document.body.removeChild(a);
  URL.revokeObjectURL(url);
}
