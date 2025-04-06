type OverviewStatisticsCardProps = {
  title: string;
  subTitle: string;
  value: string | number;
  icon: React.ReactNode;
};

const OverviewStatisticsCard = ({ title, subTitle, value, icon }: OverviewStatisticsCardProps) => {
  return (
    <div className='relative flex h-full min-h-[180px] w-fit min-w-[350px] flex-col justify-center rounded-md bg-white p-6 shadow-lg'>
      <div className='absolute right-5 top-5'>{icon}</div>
      <div className='flex h-full w-full flex-row items-center gap-4'>
        <div className='h-[95px] w-[6px] rounded-md' style={{ backgroundColor: '#FF8A4C' }} />
        <div className='flex flex-col gap-2'>
          <div className='text-md font-medium text-gray-600'>{title}</div>
          <div>
            <div className='text-4xl font-bold text-black'>{value}</div>
            <div className='flex items-center gap-2'>
              <div className='text-sm text-gray-500'>{subTitle}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OverviewStatisticsCard;
