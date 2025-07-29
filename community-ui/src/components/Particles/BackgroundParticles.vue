<canvas id="particleCanvas"></canvas>

<script>
const canvas = document.getElementById('particleCanvas');
const ctx = canvas.getContext('2d');

// 设置canvas尺寸为窗口大小
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

const particleCount = 20;
const particles = [];
let mouseX = canvas.width / 2;
let mouseY = canvas.height / 2;

// 粒子类
class Particle {
  constructor() {
    this.x = Math.random() * canvas.width;
    this.y = Math.random() * canvas.height;
    this.size = Math.random() * 5 + 2;
    this.speedX = Math.random() * 3 - 1.5;
    this.speedY = Math.random() * 3 - 1.5;
    this.color = `hsl(${Math.random() * 60 + 10}, 100%, 50%)`;
  }

  update() {
    // 向鼠标位置移动
    this.x += (mouseX - this.x) * 0.1;
    this.y += (mouseY - this.y) * 0.1;
  }

  draw() {
    ctx.fillStyle = this.color;
    ctx.beginPath();
    ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
    ctx.fill();
  }
}

// 初始化粒子
for (let i = 0; i < particleCount; i++) {
  particles.push(new Particle());
}

// 鼠标移动事件
window.addEventListener('mousemove', (e) => {
  mouseX = e.clientX;
  mouseY = e.clientY;
});

// 窗口大小改变时调整canvas大小
window.addEventListener('resize', () => {
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
});

// 动画循环
function animate() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  particles.forEach(particle => {
    particle.update();
    particle.draw();
  });

  requestAnimationFrame(animate);
}

animate();
</script>