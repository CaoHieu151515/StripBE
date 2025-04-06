import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import RatingList from './components/RatingList';

export default function RatingPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Rate Driver Page'>
        <RatingList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
