import { Modal } from 'antd';
import React, { FC } from 'react';
import { twMerge } from 'tailwind-merge';
interface CustomModalProps {
  open: boolean;
  title?: React.ReactNode;
  footer?: React.ReactNode | null;
  setOpen: (open: boolean) => void;
  onConfirm?: () => void;
  className?: string;
  children: React.ReactNode;
  okType?: 'default' | 'danger' | 'link' | 'text' | undefined;
  okText?: string;
  loading?: boolean;
}

const CustomModal: FC<CustomModalProps> = ({
  open,
  title = 'Modal Title',
  footer,
  setOpen,
  onConfirm,
  className,
  okType,
  children,
  okText,
  loading,
}) => {
  return (
    <Modal
      title={<h1 className='text-2xl font-bold text-main-blue'>{title}</h1>}
      centered
      open={open}
      footer={footer}
      className={twMerge('!w-[50vw]', className)}
      onCancel={() => setOpen(false)}
      onOk={onConfirm}
      okType={okType}
      okText={okText}
      confirmLoading={loading}
      cancelText='Đóng'>
      {children}
    </Modal>
  );
};

export default CustomModal;
