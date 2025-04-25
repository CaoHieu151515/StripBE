import { useQuery } from 'react-query';
import { packageApi } from './package.api';
import { useLocation } from 'react-router-dom';
import queryString from 'query-string';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';

const usePackageData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search, { parseNumbers: true });
  const page = +(params.page ?? 1);
  const fetchUserDataFunction = async () => {
    try {
      const response = await packageApi.getAll({
        name: params.name as string,
        price: params.price as number,
        time: params.time as number,
        page: page - 1,
        size: PAGE_SIZE,
      });
      return response;
    } catch (e) {
      console.log(e);
      throw e;
    }
  };

  // TODO: use debounce technique to prevent many calls at a short time
  const queryKey = ['packages', params];

  const {
    data: PackageData,
    refetch: refreshPackageData,
    ...rest
  } = useQuery(queryKey, fetchUserDataFunction, {
    onError: (err) => console.log('error at hook', err),
    keepPreviousData: true,
  });

  return {
    PackageData,
    refreshPackageData,
    // setSortState,
    // setKeyword,
    // setPagination,
    // setFilter,
    // keyword,
    // totalRows,
    ...rest,
  };
};

export default usePackageData;
