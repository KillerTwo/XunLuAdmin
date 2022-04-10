import { HomeOutlined, ContactsOutlined, ClusterOutlined } from '@ant-design/icons';
import { Card, Col, Divider, Row } from 'antd';
import React, {useState} from 'react';
import { GridContent } from '@ant-design/pro-layout';
import type { RouteChildrenProps } from 'react-router';
import BasicInfo from './components/BasicInfo';

import styles from './Center.less';
import {getUserInfo} from "@/services/system/login";
import {useRequest} from "@@/plugin-request/request";

export type tabKeyType = 'projects';

const operationTabList = [
  {
    key: 'projects',
    tab: (
      <span>
        项目 <span style={{ fontSize: 14 }}>(8)</span>
      </span>
    ),
  }
];

const Center: React.FC<RouteChildrenProps> = () => {
  const [tabKey, setTabKey] = useState<tabKeyType>('projects');

  //  获取用户信息
  const { data: currentUser, loading } = useRequest(() => {
    return getUserInfo();
  });
  // console.log("data: ", data);
  // const currentUser = {roles: []};

  // const loading = false;

  //  渲染用户信息
  const renderUserInfo = ({ nickName, roles, address }: Partial<{nickName: string; roles: {roleKey: string; roleName: string;}[], address: string}>) => {
    console.log(nickName, roles, address)
    // @ts-ignore
    // @ts-ignore
    return (
      <div className={styles.detail}>
        <p>
          <ContactsOutlined
            style={{
              marginRight: 8,
            }}
          />
          {nickName}
        </p>
        <p>
          <ClusterOutlined
            style={{
              marginRight: 8,
            }}
          />
          {
            roles && roles?.length > 0 ? roles[0].roleName : ''
            /*roles||[].map((ele: { roleName: any; }) => {
              return (ele.roleName);
            })*/
          }
        </p>
        <p>
          <HomeOutlined
            style={{
              marginRight: 8,
            }}
          />
          {address}
        </p>
      </div>
    );
  };

  // 渲染tab切换
  const renderChildrenByTabKey = (tabValue: tabKeyType) => {
    console.log("tabValue: ", tabValue);
    return <BasicInfo currentUser={currentUser?.user} />;
  };

  /*if (currentUser) {
    const tags = currentUser.user?.roles.map((ele: { roleKey: any; roleName: any; }) => {return {key: ele.roleKey, label: ele.roleName}});
  }*/

  return (
    <GridContent>
      <Row gutter={24}>
        <Col lg={7} md={24}>
          <Card bordered={false} style={{ marginBottom: 24 }} loading={loading}>
            {!loading && currentUser.user && (
              <div>
                <div className={styles.avatarHolder}>
                  <img alt="" src={currentUser.user.avatar} />
                  <div className={styles.name}>{currentUser.user.nickName}</div>
                  <div>{currentUser.user?.userName}</div>
                </div>
                {renderUserInfo({nickName: currentUser.user.nickName, roles:  currentUser.user.roles, address: currentUser.user.email})}
                <Divider dashed />
                {/*<TagList tags={currentUser.user.roles || []} />
                <Divider style={{ marginTop: 16 }} dashed />*/}
                {/*<div className={styles.team}>
                  <div className={styles.teamTitle}>团队</div>
                  <Row gutter={36}>
                    {currentUser.user.notice &&
                      currentUser.user.notice.map((item: { id: React.Key | null | undefined; href: any; logo: boolean | React.ReactChild | React.ReactFragment | React.ReactPortal | null | undefined; member: boolean | React.ReactChild | React.ReactFragment | React.ReactPortal | null | undefined; }) => (
                        <Col key={item.id} lg={24} xl={12}>
                          <Link to={item.href}>
                            <Avatar size="small" src={item.logo} />
                            {item.member}
                          </Link>
                        </Col>
                      ))}
                  </Row>
                </div>*/}
              </div>
            )}
          </Card>
        </Col>
        <Col lg={17} md={24}>
          <Card
            className={styles.tabsCard}
            bordered={false}
            tabList={operationTabList}
            activeTabKey={tabKey}
            onTabChange={(_tabKey: string) => {
              setTabKey(_tabKey as tabKeyType);
            }}
          >
            {!loading && currentUser.user && renderChildrenByTabKey(tabKey)}
          </Card>
        </Col>
      </Row>
    </GridContent>
  );
};
export default Center;
