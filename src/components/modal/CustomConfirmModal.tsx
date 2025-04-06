import { Button, Modal } from 'antd';
import { useTranslation } from 'react-i18next';

interface CustomConfirmModalProps {
  open: boolean;
  title: string;
  message: string;
  setOpen: (open: boolean) => void;
  className?: string;
  onConfirm?: () => void;
}

const CustomConfirmModal = ({ open, setOpen, title, message, onConfirm }: CustomConfirmModalProps) => {
  const { t: tCommon } = useTranslation('common');

  return (
    <Modal
      open={open}
      centered
      closable={false}
      footer={null}
      width={400}
      onCancel={() => setOpen(false)}
      className='rounded-lg'>
      <div className='flex flex-col items-center px-10 py-4'>
        <h3 className='text-xl font-bold'>{title}</h3>
        <p className='mb-4 mt-2 text-sm'>{message}</p>
        <Button size='large' block onClick={onConfirm}>
          {tCommon('button.check')}
        </Button>
      </div>
    </Modal>
  );
};

export default CustomConfirmModal;
