import { useQuery } from 'react-query';
import { walletApi } from './wallet.api';
import queryString from 'query-string';
import { useLocation } from 'react-router-dom';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';

const useWalletData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const page = +(params.page ?? 1);
  const fetchWalletDataFunction = async () => {
    try {
      const response = await walletApi.getAll({
        walletType: params.walletType as string,
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
  const queryKey = ['walletData', params.walletType as string, page];

  const {
    data: WalletData,
    refetch: refreshWalletData,
    ...rest
  } = useQuery(queryKey, fetchWalletDataFunction, {
    onError: (err) => console.log('error at hook', err),
  });

  return {
    WalletData,
    refreshWalletData,
    // setSortState,
    // setKeyword,
    // setPagination,
    // setFilter,
    // keyword,
    // totalRows,
    ...rest,
  };
};

export default useWalletData;
