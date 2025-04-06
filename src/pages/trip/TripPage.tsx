import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import TripList from './components/TripList';

export default function TripPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Trip'>
        <TripList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
