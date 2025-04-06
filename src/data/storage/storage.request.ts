import { STORAGE_DOMAIN_TYPE } from '@/utils/enum/storage/storage.enum';

export type StorageDomainType = STORAGE_DOMAIN_TYPE;
export type StorageContentType =
  | 'PDF'
  | 'RAR'
  | 'ZIP'
  | 'COMPRESSED_ZIP'
  | 'JPEG'
  | 'PNG'
  | 'CSV'
  | 'EXCEL'
  | 'WORD'
  | 'PPT'
  | 'PPTX'
  | 'EXCEL_XLS'
  | 'WORD_DOC'
  | 'MOV'
  | 'MP4'
  | 'WORD_DOC'
  | 'BINARY';

export type GeneratePresignedURLREQ = {
  storageDomain: StorageDomainType;
  storageContentType: StorageContentType;
};

export type GetPresignedURLREQ = {
  key: string;
};
