import { Store } from '@reduxjs/toolkit';
import { ReactNode } from 'react';
import { Provider } from 'react-redux';

export default function ReduxProvider({ children, store }: { children: ReactNode; store: Store }) {
  return <Provider store={store}>{children}</Provider>;
}
