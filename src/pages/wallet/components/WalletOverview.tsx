import CardCreditIcon from '@/components/icons/CardCreditIcon';
import SackDollarIcon from '@/components/icons/SackDollarIcon';
import OverviewStatisticsCard from '@/pages/dashboard/components/OverviewStatisticsCard';

export default function WalletOverview() {
  const walletCardsData = [
    {
      title: 'System Wallet',
      subTitle: '02410023xxx45',
      value: (500).toLocaleString('en-US'),
      icon: <SackDollarIcon className='size-6 text-primary-500' />,
    },
    {
      title: 'Temporary wallet',
      subTitle: '02410023xxx45',
      value: (250).toLocaleString('en-US'),
      icon: <CardCreditIcon className='size-6 text-primary-500' />,
    },
  ];

  return (
    <div className='hover-scrollbar flex h-full w-full max-w-pc gap-6 p-2'>
      {walletCardsData.map((card, index) => (
        <div key={index} className='flex-shrink-0'>
          <OverviewStatisticsCard title={card.title} value={card.value} subTitle={card.subTitle} icon={card.icon} />
        </div>
      ))}
    </div>
  );
}
