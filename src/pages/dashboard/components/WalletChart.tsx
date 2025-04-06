import { ApexOptions } from 'apexcharts';
import ReactApexChart from 'react-apexcharts';

const dataChart = [25, 22];

export default function WalletChart() {
  const chartOptions: ApexOptions = {
    chart: {
      type: 'donut',
    },
    colors: ['#00B4D8', '#0077B6'],
    labels: ['Deposit', 'Revenue'],
    plotOptions: {
      pie: {
        startAngle: -90,
        endAngle: 270,
        expandOnClick: true,
        donut: {
          labels: {
            show: true,
            name: {
              fontSize: '24px',
            },
            value: {
              fontSize: '24px',
            },
          },
        },
      },
    },
    dataLabels: {
      enabled: false,
    },
    fill: {
      type: 'gradient',
    },
    legend: {
      formatter: function (val, opts) {
        return val + ' - ' + opts.w.globals.series[opts.seriesIndex];
      },
    },
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: {
            width: 200,
          },
          legend: {
            position: 'bottom',
          },
        },
      },
    ],
  };

  return (
    <div className='my-2 rounded-lg bg-white p-6 shadow-md'>
      <div className='mb-5 flex justify-between gap-4'>
        <h3 className='text-xl font-semibold capitalize text-gray-900 max-sm:text-sm'>Wallet</h3>
      </div>

      <ReactApexChart options={chartOptions} series={dataChart} type='donut' height={480} width='100%' />
    </div>
  );
}
