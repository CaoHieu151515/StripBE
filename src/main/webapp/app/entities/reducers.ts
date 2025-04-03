import driver from 'app/entities/driver/driver.reducer';
import vehicle from 'app/entities/vehicle/vehicle.reducer';
import trip from 'app/entities/trip/trip.reducer';
import tripStopLocation from 'app/entities/trip-stop-location/trip-stop-location.reducer';
import passenger from 'app/entities/passenger/passenger.reducer';
import feedback from 'app/entities/feedback/feedback.reducer';
import rating from 'app/entities/rating/rating.reducer';
import packageDriver from 'app/entities/package-driver/package-driver.reducer';
import payment from 'app/entities/payment/payment.reducer';
import notification from 'app/entities/notification/notification.reducer';
import report from 'app/entities/report/report.reducer';
import systemWallet from 'app/entities/system-wallet/system-wallet.reducer';
import walletTransaction from 'app/entities/wallet-transaction/wallet-transaction.reducer';
import sendingAplication from 'app/entities/sending-aplication/sending-aplication.reducer';
import userWallet from 'app/entities/user-wallet/user-wallet.reducer';
import userDetail from 'app/entities/user-detail/user-detail.reducer';
import requestTrip from 'app/entities/request-trip/request-trip.reducer';
import driverPackageSubscription from 'app/entities/driver-package-subscription/driver-package-subscription.reducer';
import walletDeposit from 'app/entities/wallet-deposit/wallet-deposit.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  driver,
  vehicle,
  trip,
  tripStopLocation,
  passenger,
  feedback,
  rating,
  packageDriver,
  payment,
  notification,
  report,
  systemWallet,
  walletTransaction,
  sendingAplication,
  userWallet,
  userDetail,
  requestTrip,
  driverPackageSubscription,
  walletDeposit,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
