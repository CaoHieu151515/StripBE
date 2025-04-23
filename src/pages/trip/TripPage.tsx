import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import queryString from 'query-string';
import { useLocation } from 'react-router-dom';
import TripList from './components/TripList';

export default function TripPage() {
  const location = useLocation();
  const params = queryString.parse(location.search);

  return (
    <TableUtilProvider>
      <DefaultContainer
        title={params.driverId ? 'Danh sách chuyến đi của tài xế' : 'Danh sách chuyến đi'}
        breadcrumbs={[{ title: 'Trip' }]}>
        <TripList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
