import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sending-aplication.reducer';

export const SendingAplicationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sendingAplicationEntity = useAppSelector(state => state.sendingAplication.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sendingAplicationDetailsHeading">
          <Translate contentKey="sTripBeApp.sendingAplication.detail.title">SendingAplication</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sendingAplicationEntity.id}</dd>
          <dt>
            <span id="apliID">
              <Translate contentKey="sTripBeApp.sendingAplication.apliID">Apli ID</Translate>
            </span>
          </dt>
          <dd>{sendingAplicationEntity.apliID}</dd>
          <dt>
            <span id="sendApplicationType">
              <Translate contentKey="sTripBeApp.sendingAplication.sendApplicationType">Send Application Type</Translate>
            </span>
          </dt>
          <dd>{sendingAplicationEntity.sendApplicationType}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="sTripBeApp.sendingAplication.content">Content</Translate>
            </span>
          </dt>
          <dd>{sendingAplicationEntity.content}</dd>
          <dt>
            <span id="img">
              <Translate contentKey="sTripBeApp.sendingAplication.img">Img</Translate>
            </span>
          </dt>
          <dd>
            {sendingAplicationEntity.img ? (
              <div>
                {sendingAplicationEntity.imgContentType ? (
                  <a onClick={openFile(sendingAplicationEntity.imgContentType, sendingAplicationEntity.img)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {sendingAplicationEntity.imgContentType}, {byteSize(sendingAplicationEntity.img)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="sTripBeApp.sendingAplication.user">User</Translate>
          </dt>
          <dd>{sendingAplicationEntity.user ? sendingAplicationEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sending-aplication" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sending-aplication/${sendingAplicationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SendingAplicationDetail;
