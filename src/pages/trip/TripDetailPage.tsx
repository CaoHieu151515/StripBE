import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import TripDetail from './components/TripDetail';

export default function TripDetailPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer
        title='Thông tin chi tiết chuyến đi'
        breadcrumbs={[
          {
            title: 'Trip',
          },
          {
            title: 'Trip Detail',
          },
        ]}>
        <TripDetail />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
