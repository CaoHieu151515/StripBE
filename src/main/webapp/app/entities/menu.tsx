import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/driver">
        <Translate contentKey="global.menu.entities.driver" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle">
        <Translate contentKey="global.menu.entities.vehicle" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trip">
        <Translate contentKey="global.menu.entities.trip" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trip-stop-location">
        <Translate contentKey="global.menu.entities.tripStopLocation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/passenger">
        <Translate contentKey="global.menu.entities.passenger" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/feedback">
        <Translate contentKey="global.menu.entities.feedback" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rating">
        <Translate contentKey="global.menu.entities.rating" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/package-driver">
        <Translate contentKey="global.menu.entities.packageDriver" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification">
        <Translate contentKey="global.menu.entities.notification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/report">
        <Translate contentKey="global.menu.entities.report" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/system-wallet">
        <Translate contentKey="global.menu.entities.systemWallet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/system-temp-wallet">
        <Translate contentKey="global.menu.entities.systemTempWallet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/wallet-transaction">
        <Translate contentKey="global.menu.entities.walletTransaction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sending-aplication">
        <Translate contentKey="global.menu.entities.sendingAplication" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-wallet">
        <Translate contentKey="global.menu.entities.userWallet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-detail">
        <Translate contentKey="global.menu.entities.userDetail" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/request-trip">
        <Translate contentKey="global.menu.entities.requestTrip" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
