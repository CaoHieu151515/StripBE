import CustomNotistackFailMemo from '@/components/notistack/CustomNotistackFail';
import CustomNotistackInfoMemo from '@/components/notistack/CustomNotistackInfo';
import CustomNotistackSuccessMemo from '@/components/notistack/CustomNotistackSuccess';
import { SnackbarProvider } from 'notistack';
import { ReactNode } from 'react';

export default function MySnackBarProvider({ children }: { children: ReactNode }) {
  return (
    <SnackbarProvider
      autoHideDuration={5000}
      maxSnack={3}
      anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      Components={{
        success: CustomNotistackSuccessMemo,
        error: CustomNotistackFailMemo,
        info: CustomNotistackInfoMemo,
      }}>
      {children}
    </SnackbarProvider>
  );
}
