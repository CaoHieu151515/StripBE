import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { TripDTO } from '@/data/trip/dto/trip.dto';
import { useGetTripListQuery } from '@/data/trip/trip.api';
import { tripListRequestParamsToFilter } from '@/data/trip/trip.service';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { initialPagingState } from '@/utils/types/paging.type';
import queryString from 'query-string';
import { useMemo } from 'react';
import { useLocation } from 'react-router-dom';
import TripFilter from './TripFilter';
import { TripColumn } from './comlumn/TripColumn';

export default function TripList() {
  const location = useLocation();
  const params = queryString.parse(location.search);

  const page = useMemo(() => {
    const page = params[PARAM_FIELD.PAGE] || initialPagingState.page;
    if (page !== 0) return Number(page) - 1;
    return Number(page);
  }, [params]);

  const { tripListFilter } = useMemo(() => {
    const tripListFilter = tripListRequestParamsToFilter(params);
    return { tripListFilter };
  }, [params]);

  const { data, isLoading, isFetching } = useGetTripListQuery(tripListFilter);

  return (
    <div className='flex flex-col gap-5'>
      <TripFilter />
      <TableBuilder<TripDTO>
        rowKey='stripID'
        columns={TripColumn()}
        data={data?.content.map((item, idx) => ({ ...item, index: page * tripListFilter.size + idx + 1 })) ?? []}
        isLoading={isLoading || isFetching}
      />
      <CustomTablePagination
        totalItems={data?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
