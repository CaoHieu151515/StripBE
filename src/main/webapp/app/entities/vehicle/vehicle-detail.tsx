import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle.reducer';

export const VehicleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleDetailsHeading">
          <Translate contentKey="sTripBeApp.vehicle.detail.title">Vehicle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.id}</dd>
          <dt>
            <span id="vehicleID">
              <Translate contentKey="sTripBeApp.vehicle.vehicleID">Vehicle ID</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.vehicleID}</dd>
          <dt>
            <span id="vehicleType">
              <Translate contentKey="sTripBeApp.vehicle.vehicleType">Vehicle Type</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.vehicleType}</dd>
          <dt>
            <span id="vehicleImage">
              <Translate contentKey="sTripBeApp.vehicle.vehicleImage">Vehicle Image</Translate>
            </span>
          </dt>
          <dd>
            {vehicleEntity.vehicleImage ? (
              <div>
                {vehicleEntity.vehicleImageContentType ? (
                  <a onClick={openFile(vehicleEntity.vehicleImageContentType, vehicleEntity.vehicleImage)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {vehicleEntity.vehicleImageContentType}, {byteSize(vehicleEntity.vehicleImage)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="carregistration">
              <Translate contentKey="sTripBeApp.vehicle.carregistration">Carregistration</Translate>
            </span>
          </dt>
          <dd>
            {vehicleEntity.carregistration ? (
              <div>
                {vehicleEntity.carregistrationContentType ? (
                  <a onClick={openFile(vehicleEntity.carregistrationContentType, vehicleEntity.carregistration)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {vehicleEntity.carregistrationContentType}, {byteSize(vehicleEntity.carregistration)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="vehicleInspectionCertificate">
              <Translate contentKey="sTripBeApp.vehicle.vehicleInspectionCertificate">Vehicle Inspection Certificate</Translate>
            </span>
          </dt>
          <dd>
            {vehicleEntity.vehicleInspectionCertificate ? (
              <div>
                {vehicleEntity.vehicleInspectionCertificateContentType ? (
                  <a onClick={openFile(vehicleEntity.vehicleInspectionCertificateContentType, vehicleEntity.vehicleInspectionCertificate)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {vehicleEntity.vehicleInspectionCertificateContentType}, {byteSize(vehicleEntity.vehicleInspectionCertificate)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="carInsurance">
              <Translate contentKey="sTripBeApp.vehicle.carInsurance">Car Insurance</Translate>
            </span>
          </dt>
          <dd>
            {vehicleEntity.carInsurance ? (
              <div>
                {vehicleEntity.carInsuranceContentType ? (
                  <a onClick={openFile(vehicleEntity.carInsuranceContentType, vehicleEntity.carInsurance)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {vehicleEntity.carInsuranceContentType}, {byteSize(vehicleEntity.carInsurance)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="vehicleNumber">
              <Translate contentKey="sTripBeApp.vehicle.vehicleNumber">Vehicle Number</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.vehicleNumber}</dd>
          <dt>
            <span id="numberOfSeats">
              <Translate contentKey="sTripBeApp.vehicle.numberOfSeats">Number Of Seats</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.numberOfSeats}</dd>
          <dt>
            <span id="vehicleColor">
              <Translate contentKey="sTripBeApp.vehicle.vehicleColor">Vehicle Color</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.vehicleColor}</dd>
          <dt>
            <span id="vehicleBrand">
              <Translate contentKey="sTripBeApp.vehicle.vehicleBrand">Vehicle Brand</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.vehicleBrand}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="sTripBeApp.vehicle.status">Status</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.status}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.vehicle.driver">Driver</Translate>
          </dt>
          <dd>{vehicleEntity.driver ? vehicleEntity.driver.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle/${vehicleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleDetail;
