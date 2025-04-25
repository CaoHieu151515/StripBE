import { UserGetAllDTO } from '@/@types/dto/userDTO';
import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import { useNavigate } from 'react-router-dom';

export const UserColumn = (): ColumnsType<UserGetAllDTO> => {
  const navigate = useNavigate();

  return [
    // {
    //   title: () => <TableHeaderCell key='userId' label={'ID'} sortKey='userId' />,
    //   key: 'userId',
    //   render: ({ ...props }: UserGetAllDTO) => {
    //     // console.log("UserColumn");
    //     return <div>{props?.userId}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='username' label={'Full Name'} sortKey='username' />,
    //   key: 'username',
    //   render: ({ ...props }: UserGetAllDTO) => {
    //     return <div>{props?.username}</div>;
    //   },
    // },
    {
      title: () => <TableHeaderCell key='firstName' label={'Họ'} sortKey='firstName' />,
      key: 'firstName',
      render: ({ ...props }: UserGetAllDTO) => {
        return <div>{props?.firstName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='lastName' label={'Tên'} sortKey='lastName' />,
      key: 'lastName',
      render: ({ ...props }: UserGetAllDTO) => {
        return <div>{props?.lastName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='phoneNumber' label={'Số Điện Thoại'} sortKey='phoneNumber' />,
      key: 'phoneNumber',
      render: ({ ...props }: UserGetAllDTO) => {
        // return <div>{formatPhoneNumber(props?.phoneNumber)}</div>;
        return <div>{props?.phoneNumber}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='email' label={'Email'} sortKey='email' />,
      key: 'email',
      render: ({ ...props }: UserGetAllDTO) => {
        return <div>{props?.email}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='gender' label={'Giới tính'} />,
      key: 'gender',
      render: ({ ...props }: UserGetAllDTO) => {
        return <div className='capitalize'>{props?.gender}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'Trạng Thái'} />,
      key: 'status',
      render: ({ ...props }: UserGetAllDTO) => {
        return (
          <div className='capitalize'>
            {props?.active ? (
              <Tag color='success' className='w-20 text-center'>
                Active
              </Tag>
            ) : (
              <Tag color='error' className='w-20 text-center'>
                Inactive
              </Tag>
            )}
          </div>
        );
      },
    },
    // {
    //   title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
    //   key: 'status',
    //   render: ({ ...props }: UserGetAllDTO) => {
    //     return (
    //       <div className='capitalize'>
    //         {props?.status === USER_ACCOUNT_STATUS.ACTIVE ? (
    //           <Tag color='success' className='w-20 text-center'>
    //             {props?.status}
    //           </Tag>
    //         ) : (
    //           <Tag color='error' className='w-20 text-center'>
    //             {props?.status}
    //           </Tag>
    //         )}
    //       </div>
    //     );
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='role' label={'Role'} />,
    //   key: 'role',
    //   render: ({ ...props }: UserGetAllDTO) => {
    //     return <div className='capitalize'>{props?.role}</div>;
    //   },
    // },
    {
      title: () => <TableHeaderCell key='action' label={'Hành Động'} />,
      key: 'action',
      render: ({ ...props }: UserGetAllDTO) => (
        <div className='flex w-full justify-start'>
          <CustomTableActionsButton
            label={'Xem chi tiết'}
            onClick={() => navigate(MY_ROUTE.USER.VIEW_USER_ACCOUNT.detail(props?.userId.toString()))}
          />
        </div>
      ),
    },
  ];
};
