import DefaultContainer from '@/layouts/DefaultContainer';
import AccountChart from './components/AccountChart';
import Overview from './components/Overview';
import PackageChart from './components/PackageChart';
import WalletChart from './components/WalletChart';

export default function DashboardPage() {
  return (
    <DefaultContainer title='Dashboard'>
      <div className='flex max-w-full flex-col gap-6'>
        <Overview />

        <AccountChart />
        <div className='flex w-full gap-6'>
          <div className='w-2/3'>
            <PackageChart />
          </div>
          <div className='w-1/3'>
            <WalletChart />
          </div>
        </div>
      </div>
    </DefaultContainer>
  );
}
