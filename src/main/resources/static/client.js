let socket, ctx, processor, stream;

const log = document.getElementById("log");
const status = document.getElementById("status");
const startBtn = document.getElementById("start");

startBtn.onclick = async () => {
    if (socket) return;

    startBtn.disabled = true;
    log.textContent = "";
    status.textContent = "Connecting…";

    socket = new WebSocket("ws://localhost:8080/ws/audio");

    socket.onopen = () => status.textContent = "Listening…";
    socket.onclose = () => status.textContent = "Disconnected";

    socket.onmessage = e => {
        log.textContent += e.data + "\n";
        log.scrollTop = log.scrollHeight;
    };

    await startMic();
};

async function startMic() {
    stream = await navigator.mediaDevices.getUserMedia({ audio:true });
    ctx = new AudioContext();

    const source = ctx.createMediaStreamSource(stream);
    processor = ctx.createScriptProcessor(4096,1,1);

    source.connect(processor);
    processor.connect(ctx.destination);

    processor.onaudioprocess = e => {
        if (!socket || socket.readyState !== 1) return;

        const input = e.inputBuffer.getChannelData(0);
        const down = downsample(input, ctx.sampleRate, 16000);

        const pcm16 = new Int16Array(down.length);
        for (let i=0;i<down.length;i++) pcm16[i] = down[i] * 0x7fff;

        socket.send(pcm16.buffer);
    };
}

function downsample(buffer, rate, outRate) {
    if (outRate === rate) return buffer;

    const ratio = rate / outRate;
    const newLen = Math.round(buffer.length / ratio);
    const result = new Float32Array(newLen);

    let offset = 0;
    for (let i=0;i<result.length;i++) {
        const next = Math.round((i + 1) * ratio);
        let sum = 0, count = 0;
        for (let j=offset;j<next && j<buffer.length;j++) {
            sum += buffer[j]; count++;
        }
        result[i] = sum / count;
        offset = next;
    }
    return result;
}
