import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './passenger.reducer';

export const PassengerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const passengerEntity = useAppSelector(state => state.passenger.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="passengerDetailsHeading">
          <Translate contentKey="sTripBeApp.passenger.detail.title">Passenger</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.id}</dd>
          <dt>
            <span id="passengerID">
              <Translate contentKey="sTripBeApp.passenger.passengerID">Passenger ID</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.passengerID}</dd>
          <dt>
            <span id="startLoca">
              <Translate contentKey="sTripBeApp.passenger.startLoca">Start Loca</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.startLoca}</dd>
          <dt>
            <span id="endLoca">
              <Translate contentKey="sTripBeApp.passenger.endLoca">End Loca</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.endLoca}</dd>
          <dt>
            <span id="amountApproveFee">
              <Translate contentKey="sTripBeApp.passenger.amountApproveFee">Amount Approve Fee</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.amountApproveFee}</dd>
          <dt>
            <span id="luggageImg">
              <Translate contentKey="sTripBeApp.passenger.luggageImg">Luggage Img</Translate>
            </span>
          </dt>
          <dd>
            {passengerEntity.luggageImg ? (
              <div>
                {passengerEntity.luggageImgContentType ? (
                  <a onClick={openFile(passengerEntity.luggageImgContentType, passengerEntity.luggageImg)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {passengerEntity.luggageImgContentType}, {byteSize(passengerEntity.luggageImg)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="luggageDescription">
              <Translate contentKey="sTripBeApp.passenger.luggageDescription">Luggage Description</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.luggageDescription}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="sTripBeApp.passenger.type">Type</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.type}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="sTripBeApp.passenger.status">Status</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.status}</dd>
          <dt>
            <span id="pickUpTime">
              <Translate contentKey="sTripBeApp.passenger.pickUpTime">Pick Up Time</Translate>
            </span>
          </dt>
          <dd>
            {passengerEntity.pickUpTime ? <TextFormat value={passengerEntity.pickUpTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="sTripBeApp.passenger.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.endTime ? <TextFormat value={passengerEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="checkIn">
              <Translate contentKey="sTripBeApp.passenger.checkIn">Check In</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.checkIn ? 'true' : 'false'}</dd>
          <dt>
            <span id="checkInTime">
              <Translate contentKey="sTripBeApp.passenger.checkInTime">Check In Time</Translate>
            </span>
          </dt>
          <dd>
            {passengerEntity.checkInTime ? <TextFormat value={passengerEntity.checkInTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="checkOut">
              <Translate contentKey="sTripBeApp.passenger.checkOut">Check Out</Translate>
            </span>
          </dt>
          <dd>{passengerEntity.checkOut ? 'true' : 'false'}</dd>
          <dt>
            <span id="checkOutTIme">
              <Translate contentKey="sTripBeApp.passenger.checkOutTIme">Check Out T Ime</Translate>
            </span>
          </dt>
          <dd>
            {passengerEntity.checkOutTIme ? <TextFormat value={passengerEntity.checkOutTIme} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="appliedAt">
              <Translate contentKey="sTripBeApp.passenger.appliedAt">Applied At</Translate>
            </span>
          </dt>
          <dd>
            {passengerEntity.appliedAt ? <TextFormat value={passengerEntity.appliedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="sTripBeApp.passenger.trip">Trip</Translate>
          </dt>
          <dd>{passengerEntity.trip ? passengerEntity.trip.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.passenger.user">User</Translate>
          </dt>
          <dd>{passengerEntity.user ? passengerEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/passenger" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/passenger/${passengerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PassengerDetail;
