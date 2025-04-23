import { Modal } from 'antd';
import { twMerge } from 'tailwind-merge';

interface CustomConfirmModalProps {
  open: boolean;
  title: string;
  message: string;
  setOpen: (open: boolean) => void;
  className?: string;
  onConfirm?: () => void;
  okText?: string;
  cancelText?: string;
}

const CustomConfirmModal = ({
  open,
  setOpen,
  title,
  message,
  onConfirm,
  className,
  okText,
  cancelText,
}: CustomConfirmModalProps) => {
  return (
    <Modal
      open={open}
      centered
      closable={false}
      width={400}
      onCancel={() => setOpen(false)}
      okText={okText}
      onOk={onConfirm}
      cancelText={cancelText}
      className={twMerge('rounded-lg', className)}>
      <div className='flex flex-col items-center px-10 py-4'>
        <h3 className='text-xl font-bold'>{title}</h3>
        <p className='mb-4 mt-2 text-sm'>{message}</p>
      </div>
    </Modal>
  );
};

export default CustomConfirmModal;
