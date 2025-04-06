import { useGeneratePresignedURLMutation } from '@/data/storage/storage.api';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { STORAGE_DOMAIN_TYPE } from '@/utils/enum/storage/storage.enum';
import { getStorageContentType } from '@/utils/file.helper';
import { Button, Image } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { Dispatch, HTMLAttributes, SetStateAction, useCallback, useEffect, useRef } from 'react';
import { Accept, useDropzone } from 'react-dropzone';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
import TrashOutlineIcon from '../icons/TrashOutlineIcon';
import UploadFileIcon from '../icons/UploadFileIcon';

type CustomUploadFileProps = Pick<HTMLAttributes<HTMLDivElement>, 'className'> & {
  value: any[];
  onChange: (value: File[]) => void;
  setFileKeys: Dispatch<SetStateAction<string[]>>;
  isLoadingUpload: boolean;
  setIsLoadingUpload: Dispatch<SetStateAction<boolean>>;
  maxFileSizeInMB?: number;
  maxFiles?: number;
  minFiles?: number;
  accept?: Accept;
  isPreview?: boolean;
  isPreviewImage?: boolean;
  label?: string;
};

export function CustomUploadFile({
  onChange,
  value,
  setFileKeys,
  className,
  isLoadingUpload,
  setIsLoadingUpload,
  maxFileSizeInMB = 15,
  maxFiles = 3,
  accept = { 'image/png': ['.png'] },
  isPreview,
  isPreviewImage = true,
  label,
}: CustomUploadFileProps) {
  const { t: tCommon } = useTranslation('common');

  const valueRef = useRef([...value]);

  const [generatePresignedURL] = useGeneratePresignedURLMutation();

  const handleCustomUploadFiles = useCallback(
    async (file: File) => {
      setIsLoadingUpload((prev) => !prev);
      const { key, url } = await generatePresignedURL({
        storageContentType: getStorageContentType(file.type.split('/')[1]),
        storageDomain: STORAGE_DOMAIN_TYPE.ADMIN,
      }).unwrap();
      try {
        await fetch(url, {
          method: HTTP_METHOD.PUT,
          body: file,
          headers: {
            'Content-Disposition': `attachment; filename="${encodeURI(file.name)}"`,
          },
        })
          .then(() => {
            setFileKeys((prev) => [...prev, key]);
          })
          .finally(() => {
            setIsLoadingUpload(false);
          });
      } catch (error) {
        console.log(error);
      }
    },
    [generatePresignedURL, setFileKeys, setIsLoadingUpload],
  );
  const handleDrop = useCallback(
    (chosenFiles: File[]) => {
      // get all valid files from chosen files (< 2MB)
      let validFiles = chosenFiles.filter((file) => file.size <= maxFileSizeInMB * 1048576);

      // if have some files are rejected
      if (validFiles.length < chosenFiles.length) {
        enqueueSnackbar({
          message: `${tCommon('component.uploader.max_file_size_mb', { max: maxFileSizeInMB })}`,
          variant: 'error',
        });
      }

      // check max number of uploaded files
      if (valueRef.current.length + validFiles.length > maxFiles) {
        enqueueSnackbar({
          message: `${tCommon('component.uploader.maximum_files', { quantity: maxFiles })} ${tCommon(
            'component.uploader.please_remove',
          )}`,
          variant: 'error',
        });
        validFiles = validFiles.slice(0, maxFiles - valueRef.current.length);
      }

      const newFiles = [...valueRef.current];

      validFiles.forEach((newFile) => {
        let index = 1;
        let renamedFile = newFile;

        // ensure unique file names by appending index if needed
        while (
          valueRef.current.some(
            (existFile) => existFile.name === renamedFile.name && existFile.type === renamedFile.type,
          )
        ) {
          const newName = `${newFile.name.replace(/\.[^.]+$/, '')}_(${index})${newFile.name.match(/\.[^.]+$/)}`;
          index++;
          renamedFile = new File([newFile], newName, { type: newFile.type });
        }
        handleCustomUploadFiles(renamedFile);
        newFiles.push(renamedFile);
      });

      onChange(newFiles);
    },
    [handleCustomUploadFiles, maxFileSizeInMB, maxFiles, onChange, tCommon],
  );

  const { getRootProps, getInputProps, open } = useDropzone({
    onDrop: handleDrop,
    accept: accept,
  });

  const handleDelete = (index: number) => {
    const newFiles = [...value];
    if (newFiles.length === 1) {
      setFileKeys([]);
      return onChange([]);
    }
    newFiles.splice(index, 1);
    onChange(newFiles);
  };

  useEffect(() => {
    valueRef.current = [...value];
  }, [value]);

  return (
    <div className={twMerge('flex flex-col gap-4', className)}>
      <Button
        {...getRootProps()}
        onClick={(e) => {
          e.preventDefault();
          open();
        }}
        type='default'
        className='group flex w-fit justify-center px-4 py-5 text-base font-semibold'
        disabled={value.length >= maxFiles || isLoadingUpload}>
        {<UploadFileIcon className='h-4 w-4 text-gray-500 duration-200 group-hover:text-main-blue' />}
        {label && <span className='text-sm font-normal'>{label}</span>}
        <input {...getInputProps()} />
      </Button>

      {Boolean(value.length) && isPreview && (
        <div className='mx-2.5 flex flex-col gap-[22px]'>
          {value.map((file, index) => (
            <div key={`${index}-${file.name}`}>
              <div className='flex items-center justify-between gap-1'>
                {file instanceof File ? (
                  <div className='space-y-5'>
                    {isPreviewImage && (
                      <Image
                        src={URL.createObjectURL(file)}
                        alt={file.name}
                        width={200}
                        height={200}
                        className='h-20 w-auto rounded-md object-contain'
                      />
                    )}
                    <p className='flex text-sm font-normal leading-[21px] text-gray-600'>{file.name}</p>
                  </div>
                ) : (
                  <div className='space-y-5'>
                    {isPreviewImage && (
                      <Image
                        src={file.url}
                        alt={file.key}
                        width={200}
                        height={200}
                        className='h-20 w-auto rounded-md object-contain'
                      />
                    )}
                    <p className='flex text-sm font-normal leading-[21px] text-gray-600'>{file?.fileName || 'File'}</p>
                  </div>
                )}
                <div className='flex gap-4'>
                  <TrashOutlineIcon
                    className='h-5 w-5 cursor-pointer text-red-500'
                    onClick={() => {
                      handleDelete(index);
                      setIsLoadingUpload(false);
                    }}
                  />
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
