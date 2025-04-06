import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import PackageCardList from './components/PackageCardList';

export default function PackagePage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Package'>
        <PackageCardList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
