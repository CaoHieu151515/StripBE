import { UploadOutlined } from '@ant-design/icons';
import { Button, Upload, message } from 'antd';
import { useTranslation } from 'react-i18next';

export type CustomFile = {
  flag: 'FILE';
  data: File;
};

type CustomFileUploaderProps = {
  onChange: (value: CustomFile[]) => void;
  value: CustomFile[];
  maxFileSizeInMB?: number;
  maxFiles?: number;
  minFiles?: number;
  accept?: string;
  className?: string;
  hideAcceptMessage?: boolean;
};

export function CustomFileUploader({
  onChange,
  value,
  maxFileSizeInMB = 15,
  maxFiles = 3,
  accept = '.jpeg,.png',
  className,
  hideAcceptMessage,
}: CustomFileUploaderProps) {
  const { t: tCommon } = useTranslation('common');

  const handleBeforeUpload = (file: File) => {
    if (file.size > maxFileSizeInMB * 1024 * 1024) {
      message.error(`File must be smaller than ${maxFileSizeInMB}MB.`);
      return Upload.LIST_IGNORE;
    }

    if (value.length >= maxFiles) {
      message.error(`You can only upload up to ${maxFiles} files.`);
      return Upload.LIST_IGNORE;
    }

    return true;
  };

  const handleFileChange = ({ fileList }: { fileList: any }) => {
    const formattedFiles = fileList.map((file: any) => ({
      flag: 'FILE',
      data: file.originFileObj,
    }));
    onChange(formattedFiles);
  };

  const handleRemove = (file: any) => {
    const newFiles = value.filter((item) => item.data.name !== file.name);
    onChange(newFiles);
  };

  return (
    <div className={`custom-file-uploader ${className}`}>
      <Upload
        listType='picture'
        accept={accept}
        beforeUpload={handleBeforeUpload}
        onChange={handleFileChange}
        onRemove={handleRemove}
        fileList={value.map((file) => ({
          uid: file.data.name,
          name: file.data.name,
          status: 'done',
          url: URL.createObjectURL(file.data),
        }))}>
        {value.length < maxFiles && (
          <Button className='upload-area'>
            <UploadOutlined />
            {tCommon('button.upload_file')}
          </Button>
        )}
      </Upload>
      {!hideAcceptMessage && (
        <p className='mt-2 text-sm text-gray-500'>
          {tCommon('label.accepted_file_types')} {accept} (Max size: {maxFileSizeInMB} MB, Max files: {maxFiles})
        </p>
      )}
    </div>
  );
}
