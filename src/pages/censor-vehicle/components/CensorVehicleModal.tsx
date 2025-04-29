import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';
import InfoItem from '@/components/common/InfoItem';
import CustomConfirmModal from '@/components/modal/CustomConfirmModal';
import CustomModal from '@/components/modal/CustomModal';
import { censorVehicleApi } from '@/data/services/api/censorVehicle/censorVehicle.api';
import queryClient from '@/data/services/queryClient';
import { baseSchema } from '@/helpers/form-schemas/AllFormSchema';
import { CENSOR_VEHICLE_STATUS } from '@/utils/enum/censor-vehicle/censor-vehicle.enum';
import { Button, Image } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { useState } from 'react';
import * as yup from 'yup';

type CensorVehicleModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
  data: CensorVehicleDTO;
};

const reasonSchema = yup.object().shape({
  reason: baseSchema.yupString,
});

export default function CensorVehicleModal({ open, setOpen, data }: CensorVehicleModalProps) {
  const [isAccept, setIsAccept] = useState(false);
  const [isReject, setIsReject] = useState(false);

  // const [putAcceptTrip, { isLoading: isLoadingAccept }] = usePutAcceptTripMutation();
  // const [putRejectTrip, { isLoading: isLoadingReject }] = usePutRejectTripMutation();
  // const { handleSubmit, control } = useForm({
  //   resolver: yupResolver(reasonSchema),
  //   defaultValues: reasonSchema.getDefault(),
  // });

  const handleApproveTrip = async () => {
    try {
      await censorVehicleApi.approve(data?.vehicle?.vehicleID ?? '');
      // await putAcceptTrip(id as string).unwrap();
      enqueueSnackbar('Duyệt kiểm phương tiện thành công', {
        variant: 'success',
        autoHideDuration: 2000,
      });
      setIsAccept(false);
      await queryClient.invalidateQueries(['CensorVehicles']);
    } catch (error) {
      console.error('Error approving vehicle:', error);
    }
  };

  const handleRejectTrip = async () => {
    try {
      await censorVehicleApi.reject(data?.vehicle?.vehicleID ?? '');
      // await putAcceptTrip(id as string).unwrap();
      enqueueSnackbar('Huỷ kiểm duyệt thành công', {
        variant: 'success',
        autoHideDuration: 2000,
      });
      setIsReject(false);
      await queryClient.invalidateQueries(['CensorVehicles']);
    } catch (error) {
      console.error('Error approving vehicle:', error);
    }
  };

  // const onSubmit = async (reason: any) => {
  //   try {
  //     await censorVehicleApi.reject(data?.vehicle?.vehicleID ?? '');
  //     enqueueSnackbar('Huỷ kiểm duyệt thành công', {
  //       variant: 'success',
  //       autoHideDuration: 2000,
  //     });
  //   setIsReject(false);
  //   await queryClient.invalidateQueries(['CensorVehicles']);
  //   } catch (error) {
  //     console.error('Error rejecting vehicle:', error);
  //   }

  //   // putRejectTrip({ tripId: id as string, reason: reason.reason })
  //   //   .unwrap()
  //   //   .then(() => {
  //   //     enqueueSnackbar('Từ chối chuyến đi thành công', {
  //   //       variant: 'success',
  //   //       autoHideDuration: 2000,
  //   //     });
  //   //     setIsReject(false);
  //   //   })
  //   //   .catch(() => {
  //   //     enqueueSnackbar('Từ chối chuyến đi thất bại', {
  //   //       variant: 'error',
  //   //       autoHideDuration: 2000,
  //   //     });
  //   //   });
  // };
  return (
    <CustomModal
      title='Thông tin kiểm duyệt phương tiện'
      open={open}
      setOpen={setOpen}
      className='!w-[1020px]'
      footer={
        data?.vehicle?.status === CENSOR_VEHICLE_STATUS.CONFIRMING && (
          <div className='mt-4'>
            <Button className='mr-2 bg-red-500 text-white' onClick={() => setIsReject(true)}>
              Từ chối
            </Button>
            <Button
              className='float-right mr-2 bg-blue-500 text-white'
              onClick={() => setIsAccept(true)}
              // loading={isLoadingAccept}
            >
              Chấp nhận
            </Button>
          </div>
        )
      }>
      <div>
        <div className='flex w-full gap-4'>
          <Image
            src={
              data?.vehicle?.vehicleImageUrl ??
              'https://vinfast-auto-vn.net/wp-content/uploads/2022/08/VinFast-VF-8-mau-Xanh-Luc.png'
            }
            alt='vehicle'
            width={600}
            height={350}
            className='w-1/3 object-contain'
          />
          <div className='max-w-2/3 w-full'>
            <h3 className='text-lg font-bold'>Thông tin phương tiện</h3>
            <div className='mt-4 grid grid-cols-2 gap-4'>
              <InfoItem label='Loại phương tiện' value={data?.vehicle?.vehicleType} />
              {/* <InfoItem label='Vehicle Name' value={'VinFast VF8'} /> */}
              <InfoItem label='Hãng phương tiện' value={data?.vehicle?.vehicleBrand} />
              {/* <InfoItem label='Vehicle Model' value={'VF8'} /> */}
              <InfoItem label='Màu phương tiện' value={data?.vehicle?.vehicleColor} />
              <InfoItem label='Số máy' value={data?.vehicle?.vehicleNumber} />
              <InfoItem label='Số chỗ ngồi' value={data?.vehicle?.numberOfSeats} />
              <InfoItem label='Trạng thái' value={data?.vehicle?.status} />
            </div>
          </div>
        </div>
        <div className='mt-4 flex justify-between gap-5'>
          <InfoItem
            label='Giấy chứng nhận đăng ký xe'
            value={
              <Image
                src={
                  data?.vehicle?.vehicleInspectionCertificateUrl ??
                  'https://tnclerks.zendesk.com/hc/article_attachments/4409967522708/Combined_month_and_year_decal.PNG'
                }
                alt='vehicle'
                width={200}
                height={200}
                className='object-contain'
              />
            }
          />
          <InfoItem
            label='Bảo hiểm xe hơi'
            value={
              <Image
                src={
                  data?.vehicle?.carInsuranceUrl ??
                  'https://www.policybazaar.com/pblife/assets/images/pb_life_1650972275.jpg'
                }
                alt='vehicle'
                width={200}
                height={200}
                className='object-contain'
              />
            }
          />
          <InfoItem
            label='Giấy chứng nhận đăng ký'
            value={
              <Image
                src={
                  data?.vehicle?.carregistrationUrl ??
                  'https://dmv.ny.gov/sites/default/files/styles/wysiwyg/public/images/2022-01/reg_sample-340x300.png?itok=HZLA63ka'
                }
                alt='vehicle'
                width={200}
                height={200}
                className='object-contain'
              />
            }
          />
        </div>
      </div>
      <CustomConfirmModal
        open={isAccept}
        title='Xác nhận duyệt kiểm Phương tiện'
        message='Bạn có chắc chắn muốn duyệt kiểm Phương tiện này không?'
        setOpen={setIsAccept}
        onConfirm={handleApproveTrip}
        okText='Duyệt'
        cancelText='Hủy'
      />
      <CustomConfirmModal
        open={isReject}
        title='Xác nhận Từ Chối duyệt kiểm Phương tiện'
        message='Bạn có chắc chắn muốn Từ Chối duyệt kiểm Phương tiện này không?'
        setOpen={setIsReject}
        onConfirm={handleRejectTrip}
        okText='Từ Chối Duyệt Kiểm'
        cancelText='Hủy'
      />
      {/* <CustomModal
        title='Từ chối duyệt kiểm' open={isReject} setOpen={() => setIsReject(false)} className='!w-[520px]'
        footer={
          <div className='mt-6 flex justify-end gap-3'>
            <Button onClick={() => setIsReject(false)}>Hủy</Button>
            <Button type='primary' className='border-none bg-red-600 ' htmlType='submit'
            // loading={isLoadingReject}
            >
              Từ chối
            </Button>
          </div>
        }>
        <form className='mt-10 space-y-4' onSubmit={handleSubmit(onSubmit)}>
          <CustomTextFieldWithLabel
            name='reason'
            type='textarea'
            control={control}
            rows={4}
            label='Lý do từ chối'
            placeholder='Nhập lý do từ chối'
            className='w-full'
          />

        </form>
      </CustomModal> */}
    </CustomModal>
  );
}
