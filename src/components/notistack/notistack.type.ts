import { CustomContentProps } from 'notistack';

export interface CustomNotiStackProps extends CustomContentProps {
  id: string;
  allowDownload: boolean;
  anchorOrigin: {
    horizontal: 'left' | 'center' | 'right';
    vertical: 'top' | 'bottom';
  };
  hideIconVariant: boolean;
  persist: boolean;
  message: string;
  action: React.ReactNode;
  autoHideDuration: number;
}
