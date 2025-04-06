import { useQuery } from 'react-query';
import { userApi } from './users.api';

const useUsersData = () => {
  // const [pagination, setPagination] = useState<PaginationState>({
  //   pageIndex: 0,
  //   pageSize: 10,
  // });
  // const [sortState, setSortState] = useState<SortingState>([]);
  // const [keyword, setKeyword] = useState<string>();
  // const [totalRows, setTotalRows] = useState<number>(0);
  // const [filter, setFilter] = useState({});
  const fetchUserDataFunction = async () => {
    try {
      const response = await userApi.getAll();
      // console.log(response);
      return response;
    } catch (e) {
      console.log(e);
      throw e;
    }
  };

  // TODO: use debounce technique to prevent many calls at a short time
  const queryKey = ['users'];

  const {
    data: UserData,
    refetch: refreshUserData,
    ...rest
  } = useQuery(queryKey, fetchUserDataFunction, {
    onError: (err) => console.log('error at hook', err),
  });

  return {
    UserData,
    refreshUserData,
    // setSortState,
    // setKeyword,
    // setPagination,
    // setFilter,
    // keyword,
    // totalRows,
    ...rest,
  };
};

export default useUsersData;
