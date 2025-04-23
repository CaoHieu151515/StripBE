import { useQuery } from 'react-query';
import { censorDriverRequestApi } from './censorDriverRequest.api';
import { useLocation } from 'react-router-dom';
import queryString from 'query-string';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';

const useCensorDriverRequestData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const page = +(params.page ?? 1);
  const fetchCensorDriverRequestDataFunction = async () => {
    try {
      const response = await censorDriverRequestApi.getAll({
        page: page - 1,
        size: PAGE_SIZE,
      });
      // console.log(response?.content);
      return response;
    } catch (e) {
      console.log(e);
      throw e;
    }
  };

  // TODO: use debounce technique to prevent many calls at a short time
  const queryKey = ['censorDriverRequests'];

  const {
    data: CensorDriverRequestData,
    refetch: refreshCensorDriverRequestData,
    ...rest
  } = useQuery(queryKey, fetchCensorDriverRequestDataFunction, {
    onError: (err) => console.log('error at hook', err),
  });

  return {
    CensorDriverRequestData,
    refreshCensorDriverRequestData,
    // setSortState,
    // setKeyword,
    // setPagination,
    // setFilter,
    // keyword,
    // totalRows,
    ...rest,
  };
};

export default useCensorDriverRequestData;
