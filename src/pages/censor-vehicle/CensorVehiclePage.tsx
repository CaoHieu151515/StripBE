import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import CensorVehicleList from './components/CensorVehicleList';

export default function CensorVehiclePage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Censor Vehicle'>
        <CensorVehicleList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
