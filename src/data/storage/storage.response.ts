export type FileAttachment = {
  key: string;
  url: string;
  name?: string;
};

export type GeneratePresignedURLRESP = FileAttachment;

export type GetPresignedURLRESP = FileAttachment;
