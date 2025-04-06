import { StorageContentType } from '@/data/storage/storage.request';

export const getStorageContentType = (contentType: string): StorageContentType => {
  switch (contentType) {
    case 'pdf':
      return 'PDF';
    case 'vnd.rar':
      return 'RAR';
    case 'zip':
      return 'ZIP';
    case 'x-zip-compressed':
      return 'COMPRESSED_ZIP';
    case 'text/csv':
      return 'CSV';
    case 'vnd.ms-excel':
      return 'EXCEL_XLS';
    case 'vnd.openxmlformats-officedocument.spreadsheetml.sheet':
      return 'EXCEL';
    case 'msword':
      return 'WORD_DOC';
    case 'vnd.openxmlformats-officedocument.wordprocessingml.document':
      return 'WORD';
    case 'vnd.ms-powerpoint':
      return 'PPT';
    case 'vnd.openxmlformats-officedocument.presentationml.presentation':
      return 'PPTX';
    case 'octet-stream':
      return 'BINARY';
    default:
      return contentType.toUpperCase() as StorageContentType;
  }
};
