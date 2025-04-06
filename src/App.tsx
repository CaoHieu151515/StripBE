import '@/utils/theme/tailwind.css';
import './App.css';

import { RouterProvider } from 'react-router';
import { syncAccessToken } from './data/auth/auth.thunk';
import router from './helpers/router';
import { useAppDispatch } from './hooks/reduxHook';

function App() {
  const dispatch = useAppDispatch();
  dispatch(syncAccessToken());

  return (
    <RouterProvider
      router={router}
      future={{
        v7_startTransition: true,
      }}
    />
  );
}

export default App;
