<template>
  <div class="home">
    <Table rowKey="code" :columns="columns" :data-source="filterData" :pagination="pagination" :row-selection="rowSelection">
      

      <InputGroup slot="code">
        <InputGroup compact>
          <Select placeholder="排序" allowClear style="width: 60%" v-model="sortValue" :options="sorts"></Select>
          <Select allowClear style="width: 40%" v-model="orderValue" :options="orders"></Select>
        </InputGroup>
        <Select placeholder="RJ" mode="tags" style="width: 150px" v-model="selectedTags.code" :options="tags.code"></Select>
      </InputGroup>
      
      <InputGroup slot="circleAndseries" compact>
        <Select placeholder="社团" mode="tags" style="width: 150px" v-model="selectedTags.circle" :options="tags.circle"></Select>
        <Select placeholder="系列" mode="tags" style="width: 150px" v-model="selectedTags.series" :options="tags.series"></Select>
      </InputGroup>
      <Select slot="voice" placeholder="声优" mode="tags" style="width: 100px" v-model="selectedTags.voice" :options="tags.voice"></Select>

      <InputGroup slot="ageAndMiscellaneous">
        <Select placeholder="年龄" mode="tags" style="width: 100px" v-model="selectedTags.age" :options="tags.age"></Select>
        <Select placeholder="其他" mode="tags" style="width: 100px" v-model="selectedTags.miscellaneous" :options="tags.miscellaneous"></Select>
      </InputGroup>

      <Select slot="genre" placeholder="类型" mode="tags" style="width: 100px" v-model="selectedTags.genre" :options="tags.genre"></Select>
  
      <InputGroup slot="rank24h" compact><Input v-model="conditions.rank24hRange[0]" /><Input placeholder="日排名" v-model="conditions.rank24hRange[1]" /></InputGroup>
      <InputGroup slot="rank7d" compact><Input v-model="conditions.rank7dRange[0]" /><Input placeholder="周排名" v-model="conditions.rank7dRange[1]" /></InputGroup>
      <InputGroup slot="rank30d" compact><Input v-model="conditions.rank30dRange[0]" /><Input placeholder="月排名" v-model="conditions.rank30dRange[1]" /></InputGroup>
      <InputGroup slot="dlCount" compact><Input v-model="conditions.dlCountRange[0]" /><Input placeholder="贩卖数" v-model="conditions.dlCountRange[1]" /></InputGroup>
      <InputGroup slot="wishlistCount" compact><Input v-model="conditions.wishlistCountRange[0]" /><Input placeholder="收藏" v-model="conditions.wishlistCountRange[1]" /></InputGroup>
      <InputGroup slot="score" compact><Input v-model="conditions.scoreRange[0]" /><Input placeholder="分数" v-model="conditions.scoreRange[1]" /></InputGroup>
      <InputGroup slot="scoreCount" compact><Input v-model="conditions.scoreCountRange[0]" /><Input placeholder="评分数" v-model="conditions.scoreCountRange[1]" /></InputGroup>
      <InputGroup slot="reviewCount" compact><Input v-model="conditions.reviewCountRange[0]" /><Input placeholder="评论数" v-model="conditions.reviewCountRange[1]" /></InputGroup>
      <InputGroup slot="price" compact><Input v-model="conditions.priceRange[0]" /><Input placeholder="价格" v-model="conditions.priceRange[1]" /></InputGroup>
      
      <span slot="img" slot-scope="text, record">
        <a :href="`${config.urlFormat}${record.code}.html`" target="_blank"><Avatar shape="square" :size="200" :src="`${config.imgFormat}${record.code}.jpg`" /></a>
      </span>

      <span slot="circleAndseriesData" slot-scope="text, record" @contextmenu.prevent="listDir(record)" @dblclick.prevent="koeOpen(record)">
        <Tag color="blue" v-if="isHave(ihave, record.code)">我有</Tag>
        <Tag color="pink" v-if="isHave(ohas, record.code)">别人有</Tag>
        <Tag color="green" v-if="isHave(free, record.code)">当场身亡</Tag>

        {{record.title}}
                <span v-if="record.circle" style="color: red">
          {{record.circle}}
        </span>

        <span v-if="record.series" style="color: blue">
          {{record.series}}
        </span>
      </span>

      <span slot="releaseDate" slot-scope="text, record" @dblclick.prevent="copy(record, false)" @contextmenu.prevent="copy(record, true)">
        <Tag color="cyan" v-if="record.age == '全年龄'">{{record.age}}</Tag>
        <Tag color="purple" v-if="record.age == 'R-15'">{{record.age}}</Tag>
        <Tag color="pink" v-if="record.age == '18禁'">{{record.age}}</Tag>
        

        <span v-for="(item) in record.miscellaneous" :key="item">
          <br>
          <Tag color="pink">{{item}}</Tag>
        </span>
        
        <br>
        {{record.releaseDate}}
        <span v-if="record.lastDate" style="color: red">
          <br>
          {{record.lastDate}}
        </span>
      </span>

      <span slot="voiceData" slot-scope="text, record">
        <span v-for="(item) in record.voice" :key="item">
          <Tag color="orange">{{item}}</Tag>
          <br>
        </span>
      </span>

      <span slot="genreData" slot-scope="text, record">
        <span v-for="(item) in record.genre" :key="item">
          <Tag>{{item}}</Tag>
          <br>
        </span>
      </span>
    </Table>


    <Collapse>
      <CollapsePanel key="1" header="选中结果">
        <p>{{ text }}</p>
      </CollapsePanel>
    </Collapse>

    

    <Modal :visible="visible" :closable="false" @cancel="visible = false" :okButtonProps="{ style: { display: 'none' } }" :cancelButtonProps="{ style: { display: 'none' } }">
      <List bordered :data-source="dirList">
        <ListItem slot="renderItem" slot-scope="item" @click="open(item)">
          {{item.type}} =====> {{ item.path }}
        </ListItem>
      </List>
    </Modal>

    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";
import { Table, Avatar, Input, Select, Tag, Collapse, List, Modal, message } from "ant-design-vue";
import Axios from "axios";
import _ from 'underscore';
import data from "@/data/data.json"
import my from "@/data/my.json"
import dcsw from "@/data/dcsw.json"
import config from "@/data/config.json"


const columns = [
  {dataIndex: "img",scopedSlots: { customRender: "img" }, slots: { title: 'code' },},
  {dataIndex: "series",scopedSlots: { customRender: "circleAndseriesData" }, slots: { title: 'circleAndseries' } },
  {dataIndex: "releaseDate",scopedSlots: { customRender: "releaseDate" }, slots: { title: 'ageAndMiscellaneous' } },
  {dataIndex: "voice",scopedSlots: { customRender: "voiceData" }, slots: { title: 'voice' }  },
  {dataIndex: "genre",scopedSlots: { customRender: "genreData" }, slots: { title: 'genre' }},
  {dataIndex: "rank24h", slots: { title: 'rank24h' }},
  {dataIndex: "rank7d", slots: { title: 'rank7d' }},
  {dataIndex: "rank30d", slots: { title: 'rank30d' }},
  {dataIndex: "dlCount", slots: { title: 'dlCount' }},
  {dataIndex: "wishlistCount", slots: { title: 'wishlistCount' }},
  {dataIndex: "score", slots: { title: 'score' }},
  {dataIndex: "scoreCount", slots: { title: 'scoreCount' }},
  {dataIndex: "reviewCount", slots: { title: 'reviewCount' }},
  {dataIndex: "price", slots: { title: 'price' }},
];

export default {
  async created() {

    // let data = (await Axios.get("/dl/list")).data;
    this.data = data;
    this.ihave = my


    this.free = dcsw

    if (this.$route.path == '/iinput'){
      let propmpt = window.prompt();

      this.ohas = propmpt == "dcsw" ? this.free : propmpt.split(/\s*,\s*/);
    }else if (this.$route.path == '/'){
      // this.conditions.dlCountRange = [500];
    }else if (this.$route.path == '/all'){
      this.conditions.dlCountRange = [500];
    }
  },
  data() {
    return {
      visible: false,
      dirList: [],
      clicks: 0,
      clicksTimeout: null,
      config,
      pagination: {"showSizeChanger": true, "showTotal": total => `总数量 ${total}`, "defaultPageSize": 50, "pageSizeOptions": ["10", "20", "50", "100", "1000", "1000000"], "showQuickJumper": true},
      sortValue: "dlCount",
      orderValue: 1,
      sorts: [
        {label: "日排名", value: "rank24h"},
        {label: "周排名", value: "rank7d"},
        {label: "月排名", value: "rank30d"},
        {label: "贩卖数", value: "dlCount"},
        {label: "收藏", value: "wishlistCount"},
        {label: "分数", value: "score"},
        {label: "评分人数", value: "scoreCount"},
        {label: "评论人数", value: "reviewCount"},
        {label: "价格", value: "price"},
      ],
      orders: [
        {label: "降序", value: 1},
        {label: "升序", value: 0},
      ],
      selectedTags: {},
      tags:{},
      conditions: {
        
        rank24hRange: [],
        rank7dRange: [],
        rank30dRange: [],
        dlCountRange: [],
        wishlistCountRange: [],
        scoreRange: [],
        scoreCountRange: [],
        reviewCountRange: [],
        priceRange: []
      },
      ihave: [],
      ohas: [],
      free: [],
      data: [],
      columns,
      selectedRowKeys: [],
    };
  },
  computed:{
    filterData(){
      let t = this.conditions;
      let selectedTags = this.selectedTags;

      let {
          rank24hRange,
          rank7dRange,
          rank30dRange,
          dlCountRange,
          wishlistCountRange,
          scoreRange,
          scoreCountRange,
          reviewCountRange,
          priceRange
        } = JSON.parse(JSON.stringify(t))


      rank24hRange[0] = rank24hRange[0] || -1;
      rank24hRange[1] = rank24hRange[1] || 10000000;

      rank7dRange[0] = rank7dRange[0] || -1;
      rank7dRange[1] = rank7dRange[1] || 10000000;

      rank30dRange[0] = rank30dRange[0] || -1;
      rank30dRange[1] = rank30dRange[1] || 10000000;

      dlCountRange[0] = dlCountRange[0] || -1;
      dlCountRange[1] = dlCountRange[1] || 10000000;

      wishlistCountRange[0] = wishlistCountRange[0] || -1;
      wishlistCountRange[1] = wishlistCountRange[1] || 10000000;

      scoreRange[0] = scoreRange[0] || -1;
      scoreRange[0] = Number(scoreRange[0])
      scoreRange[1] = scoreRange[1] || 10000000;
      scoreRange[1] = Number(scoreRange[1])


      scoreCountRange[0] = scoreCountRange[0] || -1;
      scoreCountRange[1] = scoreCountRange[1] || 10000000;

      reviewCountRange[0] = reviewCountRange[0] || -1;
      reviewCountRange[1] = reviewCountRange[1] || 10000000;

      priceRange[0] = priceRange[0] || -1;
      priceRange[1] = priceRange[1] || 10000000;
      

      let filterd =  this.data.filter(e => rank24hRange[0] <= e.rank24h && e.rank24h <= rank24hRange[1]
        && rank7dRange[0] <= e.rank7d && e.rank7d <= rank7dRange[1]
        && rank30dRange[0] <= e.rank30d && e.rank30d <= rank30dRange[1]
        && dlCountRange[0] <= e.dlCount && e.dlCount <= dlCountRange[1]
        && wishlistCountRange[0] <= e.wishlistCount && e.wishlistCount <= wishlistCountRange[1]
        && scoreRange[0] <= Number(e.score) && Number(e.score) <= scoreRange[1]
        && scoreCountRange[0] <= e.scoreCount && e.scoreCount <= scoreCountRange[1]
        && reviewCountRange[0] <= e.reviewCount && e.reviewCount <= reviewCountRange[1]
        && priceRange[0] <= e.price && e.price <= priceRange[1]
      );
      
      // filterd = filterd.slice(filterd.length - 1)

      let keys = _.keys(selectedTags)
      
      filterd = filterd.filter(e => {
        let flag = true;

        for (const i of keys) {
          let selectedList = _.get(selectedTags, i);
          
          if(_.isEmpty(selectedList)){
            continue;
          }

          let propVal = _.get(e, i);
          if (_.isArray(propVal)){
            flag = flag && _.intersection(selectedList, propVal.map(e => e.trim())).length > 0;
          }else{
            flag = flag && _.contains(selectedList, propVal.trim());
          }
        }
        
        if(this.$route.path == '/'){
            flag = flag && _.contains(this.ihave, e.code)
        }else if(this.$route.path == '/my'){
            flag = flag && _.contains(this.ihave, e.code)
        }else if(this.$route.path == '/mynodcsw'){
            flag = flag && _.contains(this.ihave, e.code) && !_.contains(this.free, e.code)
        }else if(this.$route.path == '/iinput'){
            flag = flag && _.contains(this.ohas, e.code)
        }

        return flag;
      });      

      let code = []
      // let code = filterd.map(e => e.code);
      let circle = _.uniq(filterd.map(e => e.circle));
      let series = _.uniq(filterd.map(e => e.series));
      let voice = _.uniq(filterd.map(e => e.voice).reduce((a, b) => a.concat(b), []).map(e => e.trim()));
      let age = _.uniq(filterd.map(e => e.age));
      let miscellaneous = _.uniq(filterd.map(e => e.miscellaneous).reduce((a, b) => a.concat(b), []).map(e => e.trim()));
      let genre = _.uniq(filterd.map(e => e.genre).reduce((a, b) => a.concat(b), []).map(e => e.trim()));

      code = code.map(e => ({label: e, value: e}))
      circle = circle.map(e => ({label: e, value: e}))
      series = series.map(e => ({label: e, value: e}))
      voice = voice.map(e => ({label: e, value: e}))
      age = age.map(e => ({label: e, value: e}))
      miscellaneous = miscellaneous.map(e => ({label: e, value: e}))
      genre = genre.map(e => ({label: e, value: e}))
      
      this.tags = {code, circle, series, voice, age, miscellaneous, genre}

      let display = JSON.parse(JSON.stringify(filterd))


      if (this.sortValue){
        display = display.filter(e => e[this.sortValue] || e[this.sortValue] === 0);

        if (this.orderValue){
          display.sort((b, a) => (Number(a[this.sortValue]) - Number(b[this.sortValue])));
          }else{
          display.sort((a, b) => (Number(a[this.sortValue]) - Number(b[this.sortValue])));
        }
      }
    
      return display;
    },
    rowSelection() {
      const { selectedRowKeys } = this;

      return {
        selectedRowKeys,
        hideDefaultSelections: true,
        onChange: (selectedRowKeys, selectedRows) => {
          let text = selectedRows.map(e => e.code).reduce((a, b) => a + "," + b, "");
          if (text){
            this.text = text.substr(1)
          }
        },
        selections: [
          {
            text: '我有',
            onSelect: changableRowKeys => {
              this.selectedRowKeys = _.intersection(this.ihave, changableRowKeys);
            },
          },
          {
            text: '我没有',
            onSelect: changableRowKeys => {
              this.selectedRowKeys = _.difference(changableRowKeys, this.ihave);
            },
          },
          {
            text: '清空选项',
            onSelect: changableRowKeys => {
              this.selectedRowKeys = []
            },
          },
        ],
      }
    },
    text(){
      return this.selectedRowKeys.join(",")
    }
  },
  methods: {
    isHave(list, code){
      return _.contains(list, code)
    },
    listDir(item){
      this.visible = true
      let norj = item.code.replace("RJ", "")
      Axios.post(config.pyServer, {"koe_list": norj})
          .then(e => e.data)
          .then(e => this.dirList = e)
    },
    koeOpen(item){
      let norj = item.code.replace("RJ", "")
      Axios.post(config.pyServer, {"koe_open": norj})
    },
    open(item){
      Axios.post(config.pyServer, {"direct_open": item.path})
    },
    copy(item, without){
      if (without){navigator.clipboard.writeText(item.code.replace("RJ", ""));}else{navigator.clipboard.writeText(item.code);}
      message.destroy()
      message.info("copied")
    },
    
  },
  name: "Home",
  components: {
    HelloWorld,
    Table,
    Avatar,
    Input,
    InputGroup: Input.Group,
    Select,
    SelectOption: Select.Option,
    Tag,
    Collapse,
    CollapsePanel: Collapse.Panel,
    List,
    ListItem: List.Item,
    Modal
  },
};
</script>

<style scoped>
.ant-avatar >>> img{
  max-width: 100%;
  max-height: 100%;
}
.ant-input-group >>> input {
  width: 70px;
}
.ant-collapse >>> p {
  word-wrap: break-word;
}
</style>