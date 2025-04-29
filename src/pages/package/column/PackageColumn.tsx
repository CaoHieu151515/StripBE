import { PackageGetAllDTO } from '@/@types/dto/packageDTO';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { packageApi } from '@/data/services/api/package/package.api';
import queryClient from '@/data/services/queryClient';
import { PACKAGE_STATUS } from '@/utils/enum/package/package.enum';
import { Switch } from 'antd';
import { ColumnsType } from 'antd/es/table';

export const PackageColumn = (): ColumnsType<PackageGetAllDTO> => {
  // const [isModalDetailOpen, setIsModalDetailOpen] = useState<number | null>(null);

  const handleToggleStatus = async (packageId: string) => {
    try {
      await packageApi.toggleStatus(packageId);
      queryClient.invalidateQueries(['packages']);
    } catch (error) {
      console.log('error', error);
    }
    // await putToggleStaffStatus(userId)
    //   .unwrap()
    //   .then(() => {
    //     enqueueSnackbar('Cập nhật trạng thái thành công', {
    //       variant: 'success',
    //     });
    //   });
  };

  return [
    {
      title: () => <TableHeaderCell key='name' label={'STT'} sortKey='name' />,
      key: 'STT',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.index}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'Tên Gói'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='price' label={'Giá'} sortKey='price' />,
      key: 'price',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.price}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='time' label={'Thời gian gói'} sortKey='time' />,
      key: 'time',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.time}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='bonus' label={'Thời gian tặng kèm cho gói'} sortKey='bonus' />,
      key: 'bonus',
      render: ({ ...props }: PackageGetAllDTO) => {
        return <div>{props?.bonus}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='action' label={''} />,
      key: 'action',
      render: ({ ...props }: PackageGetAllDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <Switch
              className='ml-2'
              checked={props?.status == PACKAGE_STATUS.ACTIVE}
              // loading={isLoadingToggleStaffStatus}
              onChange={() => handleToggleStatus(props?.packageID ?? '')}
            />
            {/* <Button>Inactive</Button>
            <CustomTableActionsButton label={'Inactive'} onClick={() => setIsModalDetailOpen(props.id ?? null)} /> */}
          </div>
          {/* {isModalDetailOpen === props.id && (
                <CensorDriverDetailModal
                  open={isModalDetailOpen === props.id}
                  setOpen={() => setIsModalDetailOpen(null)}
                  data={props}
                />
              )} */}
        </>
      ),
    },
  ];
};
