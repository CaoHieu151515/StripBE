import { PackageGetAllDTO } from '@/@types/dto/packageDTO';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { ColumnsType } from 'antd/es/table';

export const PackageColumn = (): ColumnsType<PackageGetAllDTO> => {
  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'name'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='packageID' label={'Package ID'} sortKey='packageID' />,
      key: 'packageID',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.packageID}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='price' label={'Price'} sortKey='price' />,
      key: 'price',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.price}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='description' label={'Description'} sortKey='description' />,
      key: 'description',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.description}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='time' label={'time'} sortKey='time' />,
      key: 'time',
      width: 100,
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.time}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='bonus' label={'bonus'} sortKey='bonus' />,
      key: 'bonus',
      width: 100,
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.bonus}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'status'} sortKey='status' />,
      key: 'status',
      width: 120,
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.status}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='payments' label={'payments'} sortKey='payments' />,
      key: 'payments',
      width: 120,
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.payments ? 'todo' : 'null'}</div>;
      },
    },
    {
      title: () => (
        <TableHeaderCell
          key='driverPackageSubscriptions'
          label={'Driver Package Subscriptions'}
          sortKey='driverPackageSubscriptions'
        />
      ),
      width: 300,
      key: 'driverPackageSubscriptions',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.driverPackageSubscriptions ? 'todo' : 'null'}</div>;
      },
    },
    // {
    //   title: () => <TableHeaderCell key='packageName' label={'Package Name'} sortKey='packageName' />,
    //   key: 'packageName',
    //   render: ({ ...props }: PackageGetAllDTO) => {
    //     return <div>{props?.packageName}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='packageTime' label={'Package Time'} sortKey='packageTime' />,
    //   key: 'packageTime',
    //   render: ({ ...props }: PackageGetAllDTO) => {
    //     return <div>{`${props?.packageTime} ${props?.packageTime > 1 ? 'months' : 'month'}`}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='packageBonusTime' label={'Package Bonus Time'} sortKey='packageBonusTime' />,
    //   key: 'packageBonusTime',
    //   render: ({ ...props }: PackageGetAllDTO) => {
    //     return (
    //       <div>
    //         {props?.packageBonusTime
    //           ? `${props?.packageBonusTime} ${props?.packageBonusTime > 1 ? 'months' : 'month'}`
    //           : '-'}
    //       </div>
    //     );
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='packagePrice' label={'Package Price (VND)'} sortKey='packagePrice' />,
    //   key: 'packagePrice',
    //   render: ({ ...props }: PackageGetAllDTO) => {
    //     return <div>{props?.packagePrice.toLocaleString()}</div>;
    //   },
    // },
  ];
};
