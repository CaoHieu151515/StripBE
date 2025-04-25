import DefaultContainer from '@/layouts/DefaultContainer';
import { UserAccountDetail } from './components/UserAccountDetail';

export const UserAccountDetailPage = () => {
  return (
    <DefaultContainer title='Thông Tin Chi Tiết Người Dùng'>
      <UserAccountDetail />
    </DefaultContainer>
  );
};
