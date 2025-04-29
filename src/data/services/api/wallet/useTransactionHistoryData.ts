import { useQuery } from 'react-query';
import { walletApi } from './wallet.api';
import queryString from 'query-string';
import { useLocation } from 'react-router-dom';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';

const useTransactionHistoryData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const page = +(params.page ?? 1);
  const fetchTransactionHistoryDataFunction = async () => {
    try {
      const response = await walletApi.getTransactionhistory({
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
  const queryKey = ['TransactionHistory', params.walletType as string, page];

  const {
    data: TransactionHistoryData,
    refetch: refreshTransactionHistoryData,
    ...rest
  } = useQuery(queryKey, fetchTransactionHistoryDataFunction, {
    onError: (err) => console.log('error at hook', err),
    keepPreviousData: true,
    staleTime: 1000 * 60 * 5,
    cacheTime: 1000 * 60 * 10,
    refetchOnWindowFocus: false,
    refetchOnReconnect: true,
  });

  return {
    TransactionHistoryData,
    refreshTransactionHistoryData,
    // setSortState,
    // setKeyword,
    // setPagination,
    // setFilter,
    // keyword,
    // totalRows,
    ...rest,
  };
};

export default useTransactionHistoryData;
