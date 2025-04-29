import { useQuery } from 'react-query';
import { censorVehicleApi } from './censorVehicle.api';
import { useLocation } from 'react-router-dom';
import queryString from 'query-string';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';

const useCensorVehicleData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const page = +(params.page ?? 1);

  const fetchCensorVehicleDataFunction = async () => {
    try {
      const response = await censorVehicleApi.getAll({
        lastName: params.lastName as string,
        // firstName: params.firstName as string,
        // email: params.email as string,
        // phone: params.phone as string,
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
  const queryKey = ['CensorVehicles', params.lastName, page];

  const {
    data: CensorVehicleData,
    refetch: refreshCensorVehicleData,
    ...rest
  } = useQuery(queryKey, fetchCensorVehicleDataFunction, {
    onError: (err) => console.log('error at hook', err),
    keepPreviousData: true,
    staleTime: 1000 * 60 * 5,
    cacheTime: 1000 * 60 * 10,
    refetchOnWindowFocus: false,
    refetchOnReconnect: true,
  });

  return {
    CensorVehicleData,
    refreshCensorVehicleData,
    ...rest,
  };
};

export default useCensorVehicleData;
