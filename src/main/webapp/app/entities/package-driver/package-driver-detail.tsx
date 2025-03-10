import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './package-driver.reducer';

export const PackageDriverDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const packageDriverEntity = useAppSelector(state => state.packageDriver.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="packageDriverDetailsHeading">
          <Translate contentKey="sTripBeApp.packageDriver.detail.title">PackageDriver</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.id}</dd>
          <dt>
            <span id="packageID">
              <Translate contentKey="sTripBeApp.packageDriver.packageID">Package ID</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.packageID}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="sTripBeApp.packageDriver.price">Price</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.price}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="sTripBeApp.packageDriver.name">Name</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="sTripBeApp.packageDriver.description">Description</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.description}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="sTripBeApp.packageDriver.time">Time</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.time}</dd>
          <dt>
            <span id="bonus">
              <Translate contentKey="sTripBeApp.packageDriver.bonus">Bonus</Translate>
            </span>
          </dt>
          <dd>{packageDriverEntity.bonus}</dd>
        </dl>
        <Button tag={Link} to="/package-driver" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/package-driver/${packageDriverEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PackageDriverDetail;
