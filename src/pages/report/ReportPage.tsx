import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import ReportList from './components/ReportList';

export default function ReportPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Report'>
        <ReportList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
