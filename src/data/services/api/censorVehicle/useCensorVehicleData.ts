import { useQuery } from 'react-query';
import { censorVehicleApi } from './censorVehicle.api';

const useCensorVehicleData = () => {
  // const [pagination, setPagination] = useState<PaginationState>({
  //   pageIndex: 0,
  //   pageSize: 10,
  // });
  // const [sortState, setSortState] = useState<SortingState>([]);
  // const [keyword, setKeyword] = useState<string>();
  // const [totalRows, setTotalRows] = useState<number>(0);
  // const [filter, setFilter] = useState({});
  const fetchCensorVehicleDataFunction = async () => {
    try {
      const response = await censorVehicleApi.getAll();
      // console.log(response);
      return response;
    } catch (e) {
      console.log(e);
      throw e;
    }
  };

  // TODO: use debounce technique to prevent many calls at a short time
  const queryKey = ['CensorVehicles'];

  const {
    data: CensorVehicleData,
    refetch: refreshCensorVehicleData,
    ...rest
  } = useQuery(queryKey, fetchCensorVehicleDataFunction, {
    onError: (err) => console.log('error at hook', err),
  });

  return {
    CensorVehicleData,
    refreshCensorVehicleData,
    // setSortState,
    // setKeyword,
    // setPagination,
    // setFilter,
    // keyword,
    // totalRows,
    ...rest,
  };
};

export default useCensorVehicleData;
